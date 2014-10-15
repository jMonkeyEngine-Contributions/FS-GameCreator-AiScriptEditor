package com.ractoc.fs.ai.script.editor.filetype;

import com.ractoc.fs.ai.AiComponent;
import com.ractoc.fs.ai.AiScript;
import com.ractoc.fs.parsers.ai.AiComponentExit;
import com.ractoc.fs.parsers.ai.AiComponentProperty;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport.ReadWrite;
import org.openide.nodes.Sheet;

class AiComponentNode extends AbstractNode {

    AiComponentNode(Children kids) {
        super(kids);
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        Sheet.Set propertySet = Sheet.createPropertiesSet();
        Sheet.Set exitSet = Sheet.createPropertiesSet();
        propertySet.setName("properties");
        propertySet.setDisplayName("Properties");
        exitSet.setName("exits");
        exitSet.setDisplayName("Exits");
        sheet.put(propertySet);
        sheet.put(exitSet);
        AiComponent component = (AiComponent) this.getValue("component");
        AiScript script = (AiScript) this.getValue("script");
        Field[] fields = component.getClass().getDeclaredFields();
        for (Field field : fields) {
            AiComponentProperty property = field.getAnnotation(AiComponentProperty.class);
            if (property != null) {
                component.initialise(null, null, script);
                propertySet.put(new PropertyValue(component, field, property));
            } else {
                AiComponentExit exit = field.getAnnotation(AiComponentExit.class);
                if (exit != null) {
                    exitSet.put(new ExitValue(component, field, exit));
                }
            }
        }
        return sheet;
    }

    private class PropertyValue extends ReadWrite<Object> {

        private final Field field;
        private final AiComponent aiComponent;

        private PropertyValue(AiComponent aiComponent, Field field, AiComponentProperty property) {
            super(property.name(), property.type(), property.displayName(), property.shortDescription());
            this.aiComponent = aiComponent;
            this.field = field;
            field.setAccessible(true);
        }

        @Override
        public Object getValue() throws IllegalAccessException {
            Object value = field.get(aiComponent);
            System.out.println(field.getName() + ": " + value);
            return field.get(aiComponent);
        }

        @Override
        public void setValue(Object value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            field.set(aiComponent, value);
            aiComponent.updateProperties();
        }

        @Override
        public Class<Object> getValueType() {
            return (Class<Object>) field.getType();
        }
    }

    private class ExitValue extends ReadWrite<Object> {

        private final Field field;
        private final AiComponent aiComponent;

        private ExitValue(AiComponent aiComponent, Field field, AiComponentExit exit) {
            super(exit.name(), exit.type(), exit.displayName(), exit.shortDescription());
            this.aiComponent = aiComponent;
            this.field = field;
            field.setAccessible(true);
        }

        @Override
        public Object getValue() throws IllegalAccessException {
            Object value = field.get(aiComponent);
            System.out.println(field.getName() + ": " + value);
            return field.get(aiComponent);
        }

        @Override
        public void setValue(Object value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            field.set(aiComponent, value);
            aiComponent.updateProperties();
        }

        @Override
        public Class<Object> getValueType() {
            return (Class<Object>) field.getType();
        }
    }
}
