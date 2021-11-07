package com.avispa.microf.model.property;

import com.avispa.ecm.model.configuration.propertypage.PropertyPage;
import com.avispa.ecm.model.configuration.propertypage.controls.Control;
import com.avispa.ecm.model.configuration.propertypage.controls.OrganizationControl;
import com.avispa.ecm.model.configuration.propertypage.controls.PropertyControl;
import com.avispa.ecm.model.configuration.propertypage.controls.type.OrganizationControlType;
import com.avispa.ecm.model.configuration.propertypage.controls.type.PropertyControlType;
import com.avispa.ecm.model.document.Document;
import com.avispa.ecm.util.expression.ExpressionResolver;
import com.avispa.microf.model.property.mapper.OrganizationControlMapper;
import com.avispa.microf.model.property.mapper.PropertyControlMapper;
import com.avispa.microf.model.property.mapper.PropertyPageMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Rafał Hiszpański
 */
class PropertyPageMapperIntegrationTest {
    private static final PropertyPageMapper mapper = Mappers.getMapper(PropertyPageMapper.class);

    @BeforeAll
    static void init() {
        OrganizationControlMapper orgMapper = Mappers.getMapper(OrganizationControlMapper.class);
        ExpressionResolver expressionResolver = new ExpressionResolver();
        ReflectionTestUtils.setField(orgMapper, "expressionResolver", expressionResolver);

        PropertyControlMapper propMapper = Mappers.getMapper(PropertyControlMapper.class);

        ReflectionTestUtils.setField(mapper, "organizationControlMapper", orgMapper);
        ReflectionTestUtils.setField(mapper, "propertyControlMapper", propMapper);
    }

    @Test
    void givenPropertyPageToDto_whenMaps_thenCorrect() {
        PropertyPage propertyPage = new PropertyPage();

        List<Control> controls = new ArrayList<>(2);

        PropertyControl objectName = new PropertyControl();
        objectName.setType(PropertyControlType.TEXT);
        objectName.setName("objectName");
        objectName.setLabel("Object name");
        Map<String, String> attributes = new HashMap<>();
        attributes.put("attr", "val");
        attributes.put("attr2", "val2");
        objectName.setAttributes(attributes);
        controls.add(objectName);

        OrganizationControl label = new OrganizationControl();
        label.setType(OrganizationControlType.LABEL);
        label.setLabel("'Label'");
        controls.add(label);

        propertyPage.setControls(controls);

        Document document = createDocument();

        PropertyPageDto propertyPageDto = mapper.convertToDto(propertyPage, document);

        assertEquals(propertyPage.getControls().size(), propertyPageDto.getControls().size());

        ControlDto controlDto = propertyPageDto.getControls().get(0);
        assertEquals("Object name", controlDto.getLabel());
        assertEquals("text", controlDto.getType());

        controlDto = propertyPageDto.getControls().get(1);
        assertEquals("Label", controlDto.getLabel());
        assertEquals("label", controlDto.getType());
    }

    private Document createDocument() {
        Document document = new Document();
        document.setObjectName("It's me");
        return document;
    }
}