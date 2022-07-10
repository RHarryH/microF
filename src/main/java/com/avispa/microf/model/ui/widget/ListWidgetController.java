package com.avispa.microf.model.ui.widget;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.document.Document;
import com.avispa.ecm.model.type.Type;
import com.avispa.ecm.model.type.TypeService;
import com.avispa.ecm.util.reflect.PropertyUtils;
import com.avispa.microf.model.base.IBaseService;
import com.avispa.microf.model.base.dto.Dto;
import com.avispa.microf.model.ui.configuration.columns.ListWidgetConfig;
import com.avispa.microf.model.ui.configuration.columns.ListWidgetRepository;
import com.avispa.microf.util.TypeNameUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Rafał Hiszpański
 */
@Controller
@RequestMapping("/widget")
@RequiredArgsConstructor
@Slf4j
public class ListWidgetController {
    private final TypeService typeService;
    private final ListWidgetRepository listWidgetRepository;
    private final List<IBaseService<? extends EcmObject, ? extends Dto>> services;

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex) {
        //Do something additional if required
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error/widgetError :: widgetError");
        modelAndView.addObject("errorMessage", ex.getMessage());
        modelAndView.addObject("widgetId", "properties-widget");
        return modelAndView;
    }

    @GetMapping("/list-widget/{type}")
    public String getListWidget(@PathVariable("type") String resourceId, Model model) {
        String typeName = TypeNameUtils.convertResourceIdToTypeName(resourceId);
        Type type = this.typeService.getType(typeName);

        ListWidgetConfig listWidgetConfig = listWidgetRepository.findByType(type).orElseThrow();

        List<EcmObject> ecmObjects = findAll(type.getEntityClass());

        ListWidgetDto listWidgetDto = new ListWidgetDto();
        listWidgetDto.setResourceId(resourceId);
        listWidgetDto.setTypeName(typeName);
        listWidgetDto.setCaption(listWidgetConfig.getCaption());
        listWidgetDto.setEmptyMessage(listWidgetConfig.getEmptyMessage());
        listWidgetDto.setDocument(Document.class.isAssignableFrom(type.getEntityClass()));
        listWidgetDto.setHeaders(listWidgetConfig.getProperties());

        List<ListDataDto> listDataDtos = new ArrayList<>(ecmObjects.size());
        for(EcmObject object : ecmObjects) {
            Map<String, Object> map = PropertyUtils.introspect(object);
            ListDataDto listDataDto = new ListDataDto();
            listDataDto.setId(object.getId());
            listDataDto.setHasPdfRendition(object.hasPdfRendition());

            listDataDto.setValues(listWidgetConfig.getProperties().stream()
                    .map(map::get)
                    .map(Objects::toString)
                    .collect(Collectors.toList())
            );
            listDataDtos.add(listDataDto);
        }
        listWidgetDto.setDataList(listDataDtos);

        model.addAttribute("listWidgetDto", listWidgetDto);
        return "fragments/widgets/list-widget :: list-widget";
    }

    @SuppressWarnings("unchecked")
    public List<EcmObject> findAll(Class<? extends EcmObject> entityClass) {
        IBaseService<EcmObject, Dto> foundService =
                (IBaseService<EcmObject, Dto>) services.stream().filter(m -> {
                    Class<?> serviceEntityClass = getServiceEntity(m.getClass());
                    return entityClass.equals(serviceEntityClass);
                }).findFirst().orElseThrow();

        return foundService.findAll();
    }

    private Class<?> getServiceEntity(Class<?> serviceClass) {
        Map<TypeVariable<?>, java.lang.reflect.Type> typeArgs  = TypeUtils.getTypeArguments(serviceClass, IBaseService.class);
        TypeVariable<?> argTypeParam =  IBaseService.class.getTypeParameters()[0];
        java.lang.reflect.Type argType = typeArgs.get(argTypeParam);
        return TypeUtils.getRawType(argType, null);
    }
}
