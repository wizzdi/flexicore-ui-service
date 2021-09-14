package com.flexicore.ui;


import com.flexicore.ui.app.App;
import com.flexicore.ui.model.GridPreset;
import com.flexicore.ui.request.GridPresetCreate;
import com.flexicore.ui.request.GridPresetFiltering;
import com.flexicore.ui.request.GridPresetUpdate;
import com.wizzdi.flexicore.boot.dynamic.invokers.model.DynamicExecution;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")

public class GridPresetControllerTest {

    private GridPreset gridPreset;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private DynamicExecution dynamicExecution;

    @BeforeAll
    private void init() {
        restTemplate.getRestTemplate().setInterceptors(
                Collections.singletonList((request, body, execution) -> {
                    request.getHeaders()
                            .add("authenticationKey", "fake");
                    return execution.execute(request, body);
                }));

    }

    @Test
    @Order(1)
    public void testGridPresetCreate() {
        String name = UUID.randomUUID().toString();
        GridPresetCreate request = new GridPresetCreate()
                .setDynamicExecutionId(dynamicExecution.getId())
                .setJsonNode(Map.of("test","test"))
                .setName(name);
        ResponseEntity<GridPreset> gridPresetResponse = this.restTemplate.postForEntity("/plugins/GridPresets/createGridPreset", request, GridPreset.class);
        Assertions.assertEquals(200, gridPresetResponse.getStatusCodeValue());
        gridPreset = gridPresetResponse.getBody();
        assertGridPreset(request, gridPreset);

    }

    @Test
    @Order(2)
    public void testGetAllGridPresets() {
        GridPresetFiltering request=new GridPresetFiltering();
        ParameterizedTypeReference<PaginationResponse<GridPreset>> t= new ParameterizedTypeReference<>() {
        };

        ResponseEntity<PaginationResponse<GridPreset>> gridPresetResponse = this.restTemplate.exchange("/plugins/GridPresets/getAllGridPresets", HttpMethod.POST, new HttpEntity<>(request), t);
        Assertions.assertEquals(200, gridPresetResponse.getStatusCodeValue());
        PaginationResponse<GridPreset> body = gridPresetResponse.getBody();
        Assertions.assertNotNull(body);
        List<GridPreset> gridPresets = body.getList();
        Assertions.assertNotEquals(0,gridPresets.size());
        Assertions.assertTrue(gridPresets.stream().anyMatch(f->f.getId().equals(gridPreset.getId())));


    }

    public void assertGridPreset(GridPresetCreate request, GridPreset gridPreset) {
        Assertions.assertNotNull(gridPreset);
        if(request.getName()!=null){
            Assertions.assertEquals(request.getName(), gridPreset.getName());
        }

        if(request.getDynamicExecutionId()!=null){
            Assertions.assertEquals(request.getDynamicExecutionId(),gridPreset.getDynamicExecution().getId());
        }
        if(request.any()!=null){
            Assertions.assertNotNull(gridPreset.any());
            for (Map.Entry<String, Object> stringObjectEntry : request.any().entrySet()) {
                Object val = gridPreset.any().get(stringObjectEntry.getKey());
                Assertions.assertNotNull(val);
                Assertions.assertEquals(stringObjectEntry.getValue(),val);
            }
        }
    }

    @Test
    @Order(3)
    public void testGridPresetUpdate(){
        String name = UUID.randomUUID().toString();
        GridPresetUpdate request = new GridPresetUpdate()
                .setId(gridPreset.getId())
                .setName(name);
        ResponseEntity<GridPreset> gridPresetResponse = this.restTemplate.exchange("/plugins/GridPresets/updateGridPreset",HttpMethod.PUT, new HttpEntity<>(request), GridPreset.class);
        Assertions.assertEquals(200, gridPresetResponse.getStatusCodeValue());
        gridPreset = gridPresetResponse.getBody();
        assertGridPreset(request, gridPreset);

    }

}
