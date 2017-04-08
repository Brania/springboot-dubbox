/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.birt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.core.internal.registry.RegistryProviderFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.ByteArrayOutputStream;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午6:17
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
@Service
@Slf4j
public class BIRTReportRunner implements ReportRunner {

    private IReportEngine birtReportEngine = null;

    // 报表文件目录
    @Value("${birt.report.dir}")
    private String reportDir;
    // 报表日志目录
    @Value("${birt.logs.dir}")
    private String reportLogDir;


    /**
     * Starts up and configures the BIRT Report Engine
     */
    @PostConstruct
    public void startUp() {
        try {
            EngineConfig engineConfig = new EngineConfig();
            engineConfig.setLogConfig(reportDir, Level.SEVERE);
            // Required due to a bug in BIRT that occurs in calling Startup after the Platform has already been started up
            RegistryProviderFactory.releaseDefault();
            Platform.startup(engineConfig);
            IReportEngineFactory reportEngineFactory = (IReportEngineFactory) Platform.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
            birtReportEngine = reportEngineFactory.createReportEngine(engineConfig);
            log.info("Birt Startup Successfully ;;;;");
        } catch (BirtException e) {
            log.error("Birt Startup Error: {}", e.getMessage());
        }
    }

    /**
     * Shuts down the BIRT Report Engine
     */
    @PreDestroy
    public void shutdown() {
        birtReportEngine.destroy();
        RegistryProviderFactory.releaseDefault();
        Platform.shutdown();
    }

    /**
     * get the path to the report design file
     *
     * @param reportName
     * @return
     * @throws RuntimeException
     */
    private File getReportFromFilesystem(String reportName) throws RuntimeException {
        Path birtReport = Paths.get(reportDir + File.separator + reportName + ".rptdesign");
        if (!Files.isReadable(birtReport))
            throw new RuntimeException("Report " + reportName + " either did not exist or was not writable.");

        return birtReport.toFile();
    }


    @Override
    public ByteArrayOutputStream runReport(Report birtReport) {

        log.info("runReport start ;;;;");

        ByteArrayOutputStream byteArrayOutputStream;
        File rptDesignFile;

        try {
            rptDesignFile = getReportFromFilesystem(birtReport.getName());
        } catch (Exception e) {
            log.error("Error while loading rptdesign: {}.", e.getMessage());
            throw new RuntimeException("Could not find report");
        }

        // process any additional parameters
        Map<String, String> parsedParameters = parseParametersAsMap(birtReport.getParameters());

        byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            IReportRunnable reportDesign = birtReportEngine.openReportDesign(rptDesignFile.getPath());
            IRunTask runTask = birtReportEngine.createRunTask(reportDesign);

            if (parsedParameters.size() > 0) {
                for (Map.Entry<String, String> entry : parsedParameters.entrySet()) {
                    runTask.setParameterValue(entry.getKey(), entry.getValue());
                }
            }
            runTask.validateParameters();

            String rptdocument = reportDir + File.separator
                    + "generated" + File.separator
                    + birtReport.getName() + ".rptdocument";
            log.info("runTask before ;;;;");
            runTask.run(rptdocument);
            log.info("runTask after ;;;;");
            IReportDocument reportDocument = birtReportEngine.openReportDocument(rptdocument);
            IRenderTask renderTask = birtReportEngine.createRenderTask(reportDocument);

            PDFRenderOption pdfRenderOption = new PDFRenderOption();
            pdfRenderOption.setOption(IPDFRenderOption.REPAGINATE_FOR_PDF, Boolean.TRUE);
            pdfRenderOption.setOutputFormat("pdf");
            pdfRenderOption.setOutputStream(byteArrayOutputStream);
            renderTask.setRenderOption(pdfRenderOption);

            renderTask.render();
            renderTask.close();
        } catch (EngineException e) {
            log.error("Error while running report task: {}.", e.getMessage());
            throw new RuntimeException();
        }

        return byteArrayOutputStream;
    }

    /**
     * Takes a String of parameters started by '?', delimited by '&', and with
     * keys and values split by '=' and returnes a Map of the keys and values
     * in the String.
     *
     * @param reportParameters a String from a HTTP request URL
     * @return a map of parameters with Key,Value entries as strings
     */
    private Map<String, String> parseParametersAsMap(String reportParameters) {
        Map<String, String> parsedParameters = new HashMap<>();
        String[] paramArray;
        if (reportParameters.isEmpty()) {
            throw new IllegalArgumentException("Report parameters cannot be empty");
        } else if (!reportParameters.startsWith("?") && !reportParameters.contains("?")) {
            throw new IllegalArgumentException("Report parameters must start with a question mark '?'!");
        } else {
            String noQuestionMark = reportParameters.substring(1, reportParameters.length());
            paramArray = noQuestionMark.split("&");
            for (String param : paramArray) {
                String[] paramGroup = param.split("=");
                if (paramGroup.length == 2) {
                    parsedParameters.put(paramGroup[0], paramGroup[1]);
                } else {
                    parsedParameters.put(paramGroup[0], "");
                }

            }
        }
        return parsedParameters;
    }


}
