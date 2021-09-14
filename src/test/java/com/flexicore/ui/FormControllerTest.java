package com.flexicore.ui;


import com.flexicore.ui.app.App;
import com.flexicore.ui.model.Form;
import com.flexicore.ui.request.FormCreate;
import com.flexicore.ui.request.FormFiltering;
import com.flexicore.ui.request.FormUpdate;
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

public class FormControllerTest {

    private Form form;
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
    public void testFormCreate() {
        String name = UUID.randomUUID().toString();
        FormCreate request = new FormCreate()
                .setDynamicExecutionId(dynamicExecution.getId())
                .setJsonNode(Map.of("test","test"))
                .setName(name);
        ResponseEntity<Form> formResponse = this.restTemplate.postForEntity("/plugins/Forms/createForm", request, Form.class);
        Assertions.assertEquals(200, formResponse.getStatusCodeValue());
        form = formResponse.getBody();
        assertForm(request, form);

    }

    @Test
    @Order(2)
    public void testGetAllForms() {
        FormFiltering request=new FormFiltering();
        ParameterizedTypeReference<PaginationResponse<Form>> t= new ParameterizedTypeReference<>() {
        };

        ResponseEntity<PaginationResponse<Form>> formResponse = this.restTemplate.exchange("/plugins/Forms/getAllForms", HttpMethod.POST, new HttpEntity<>(request), t);
        Assertions.assertEquals(200, formResponse.getStatusCodeValue());
        PaginationResponse<Form> body = formResponse.getBody();
        Assertions.assertNotNull(body);
        List<Form> forms = body.getList();
        Assertions.assertNotEquals(0,forms.size());
        Assertions.assertTrue(forms.stream().anyMatch(f->f.getId().equals(form.getId())));


    }

    public void assertForm(FormCreate request, Form form) {
        Assertions.assertNotNull(form);
        if(request.getName()!=null){
            Assertions.assertEquals(request.getName(), form.getName());
        }

        if(request.getDynamicExecutionId()!=null){
            Assertions.assertEquals(request.getDynamicExecutionId(),form.getDynamicExecution().getId());
        }
        if(request.any()!=null){
            Assertions.assertNotNull(form.any());
            for (Map.Entry<String, Object> stringObjectEntry : request.any().entrySet()) {
                Object val = form.any().get(stringObjectEntry.getKey());
                Assertions.assertNotNull(val);
                Assertions.assertEquals(stringObjectEntry.getValue(),val);
            }
        }
    }

    @Test
    @Order(3)
    public void testFormUpdate(){
        String name = UUID.randomUUID().toString();
        FormUpdate request = new FormUpdate()
                .setId(form.getId())
                .setName(name);
        ResponseEntity<Form> formResponse = this.restTemplate.exchange("/plugins/Forms/updateForm",HttpMethod.PUT, new HttpEntity<>(request), Form.class);
        Assertions.assertEquals(200, formResponse.getStatusCodeValue());
        form = formResponse.getBody();
        assertForm(request, form);

    }

}
