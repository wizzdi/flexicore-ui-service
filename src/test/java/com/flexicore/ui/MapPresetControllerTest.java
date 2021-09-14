package com.flexicore.ui;


import com.flexicore.ui.app.App;
import com.flexicore.ui.model.MapPreset;
import com.flexicore.ui.request.MapPresetCreate;
import com.flexicore.ui.request.MapPresetFiltering;
import com.flexicore.ui.request.MapPresetUpdate;
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

public class MapPresetControllerTest {

    private MapPreset mapPreset;
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
    public void testMapPresetCreate() {
        String name = UUID.randomUUID().toString();
        MapPresetCreate request = new MapPresetCreate()
                .setLatCenter(2d)
                .setLonCenter(3d)
                .setJsonNode(Map.of("test","test"))
                .setName(name);
        ResponseEntity<MapPreset> mapPresetResponse = this.restTemplate.postForEntity("/plugins/MapPresets/createMapPreset", request, MapPreset.class);
        Assertions.assertEquals(200, mapPresetResponse.getStatusCodeValue());
        mapPreset = mapPresetResponse.getBody();
        assertMapPreset(request, mapPreset);

    }

    @Test
    @Order(2)
    public void testGetAllMapPresets() {
        MapPresetFiltering request=new MapPresetFiltering();
        ParameterizedTypeReference<PaginationResponse<MapPreset>> t= new ParameterizedTypeReference<>() {
        };

        ResponseEntity<PaginationResponse<MapPreset>> mapPresetResponse = this.restTemplate.exchange("/plugins/MapPresets/getAllMapPresets", HttpMethod.POST, new HttpEntity<>(request), t);
        Assertions.assertEquals(200, mapPresetResponse.getStatusCodeValue());
        PaginationResponse<MapPreset> body = mapPresetResponse.getBody();
        Assertions.assertNotNull(body);
        List<MapPreset> mapPresets = body.getList();
        Assertions.assertNotEquals(0,mapPresets.size());
        Assertions.assertTrue(mapPresets.stream().anyMatch(f->f.getId().equals(mapPreset.getId())));


    }

    public void assertMapPreset(MapPresetCreate request, MapPreset mapPreset) {
        Assertions.assertNotNull(mapPreset);
        if(request.getName()!=null){
            Assertions.assertEquals(request.getName(), mapPreset.getName());
        }
        if(request.getLatCenter()!=null){
            Assertions.assertEquals(request.getLatCenter(),mapPreset.getLatCenter());
        }
        if(request.getLonCenter()!=null){
            Assertions.assertEquals(request.getLonCenter(),mapPreset.getLonCenter());
        }
        if(request.any()!=null){
            Assertions.assertNotNull(mapPreset.any());
            for (Map.Entry<String, Object> stringObjectEntry : request.any().entrySet()) {
                Object val = mapPreset.any().get(stringObjectEntry.getKey());
                Assertions.assertNotNull(val);
                Assertions.assertEquals(stringObjectEntry.getValue(),val);
            }
        }
    }

    @Test
    @Order(3)
    public void testMapPresetUpdate(){
        String name = UUID.randomUUID().toString();
        MapPresetUpdate request = new MapPresetUpdate()
                .setId(mapPreset.getId())
                .setName(name);
        ResponseEntity<MapPreset> mapPresetResponse = this.restTemplate.exchange("/plugins/MapPresets/updateMapPreset",HttpMethod.PUT, new HttpEntity<>(request), MapPreset.class);
        Assertions.assertEquals(200, mapPresetResponse.getStatusCodeValue());
        mapPreset = mapPresetResponse.getBody();
        assertMapPreset(request, mapPreset);

    }

}
