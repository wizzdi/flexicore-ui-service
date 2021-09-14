package com.flexicore.ui.app;

import com.flexicore.security.SecurityContextBase;
import com.flexicore.ui.model.Form;
import com.flexicore.ui.model.GridPreset;
import com.flexicore.ui.request.FormCreate;
import com.flexicore.ui.request.GridPresetCreate;
import com.flexicore.ui.service.FormService;
import com.flexicore.ui.service.GridPresetService;
import com.wizzdi.dynamic.properties.converter.DynamicPropertiesModule;
import com.wizzdi.dynamic.properties.converter.JsonConverterImplementationHolder;
import com.wizzdi.flexicore.boot.dynamic.invokers.model.DynamicExecution;
import com.wizzdi.flexicore.boot.dynamic.invokers.request.DynamicExecutionCreate;
import com.wizzdi.flexicore.boot.dynamic.invokers.service.DynamicExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class TestEntities {

    @Autowired
    private JsonConverterImplementationHolder jsonConverterImplementationHolder;

    @Autowired
    private SecurityContextBase adminSecurityContext;
    @Autowired
    private FormService formService;
    @Autowired
    private DynamicExecutionService dynamicExecutionService;
    @Autowired
    private GridPresetService gridPresetService;
    @Bean
    public DynamicExecution dynamicExecution(){
        return dynamicExecutionService.createDynamicExecution(new DynamicExecutionCreate().setName("test"),adminSecurityContext);
    }
    @Bean
    public Form form(DynamicExecution dynamicExecution){
        return formService.createForm(new FormCreate().setDynamicExecution(dynamicExecution).setJsonNode(Map.of("test","test")).setName("test"),adminSecurityContext);
    }

    @Bean
    public GridPreset gridPreset(DynamicExecution dynamicExecution){
        return gridPresetService.createGridPreset(new GridPresetCreate().setDynamicExecution(dynamicExecution).setJsonNode(Map.of("test","test")).setName("test"),adminSecurityContext);
    }


}
