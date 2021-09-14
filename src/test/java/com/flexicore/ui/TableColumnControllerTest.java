package com.flexicore.ui;


import com.flexicore.ui.app.App;
import com.flexicore.ui.model.GridPreset;
import com.flexicore.ui.model.TableColumn;
import com.flexicore.ui.request.TableColumnCreate;
import com.flexicore.ui.request.TableColumnFiltering;
import com.flexicore.ui.request.TableColumnUpdate;
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
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")

public class TableColumnControllerTest {

    private TableColumn tableColumn;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private GridPreset gridPreset;

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
    public void testTableColumnCreate() {
        String name = UUID.randomUUID().toString();
        TableColumnCreate request = new TableColumnCreate()
                .setPresetId(gridPreset.getId())
                .setName(name);
        ResponseEntity<TableColumn> tableColumnResponse = this.restTemplate.postForEntity("/plugins/TableColumns/createTableColumn", request, TableColumn.class);
        Assertions.assertEquals(200, tableColumnResponse.getStatusCodeValue());
        tableColumn = tableColumnResponse.getBody();
        assertTableColumn(request, tableColumn);

    }

    @Test
    @Order(2)
    public void testGetAllTableColumns() {
        TableColumnFiltering request=new TableColumnFiltering();
        ParameterizedTypeReference<PaginationResponse<TableColumn>> t= new ParameterizedTypeReference<>() {
        };

        ResponseEntity<PaginationResponse<TableColumn>> tableColumnResponse = this.restTemplate.exchange("/plugins/TableColumns/getAllTableColumns", HttpMethod.POST, new HttpEntity<>(request), t);
        Assertions.assertEquals(200, tableColumnResponse.getStatusCodeValue());
        PaginationResponse<TableColumn> body = tableColumnResponse.getBody();
        Assertions.assertNotNull(body);
        List<TableColumn> tableColumns = body.getList();
        Assertions.assertNotEquals(0,tableColumns.size());
        Assertions.assertTrue(tableColumns.stream().anyMatch(f->f.getId().equals(tableColumn.getId())));


    }

    public void assertTableColumn(TableColumnCreate request, TableColumn tableColumn) {
        Assertions.assertNotNull(tableColumn);
        if(request.getName()!=null){
            Assertions.assertEquals(request.getName(), tableColumn.getName());
        }
        if(request.getPresetId()!=null){
            Assertions.assertEquals(request.getPresetId(), tableColumn.getPreset().getId());
        }
    }

    @Test
    @Order(3)
    public void testTableColumnUpdate(){
        String name = UUID.randomUUID().toString();
        TableColumnUpdate request = new TableColumnUpdate()
                .setId(tableColumn.getId())
                .setName(name);
        ResponseEntity<TableColumn> tableColumnResponse = this.restTemplate.exchange("/plugins/TableColumns/updateTableColumn",HttpMethod.PUT, new HttpEntity<>(request), TableColumn.class);
        Assertions.assertEquals(200, tableColumnResponse.getStatusCodeValue());
        tableColumn = tableColumnResponse.getBody();
        assertTableColumn(request, tableColumn);

    }

}
