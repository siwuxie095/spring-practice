package com.siwuxie095.spring.chapter20th.example2nd.config;

import com.siwuxie095.spring.chapter20th.example2nd.web.SpittleController;
import com.siwuxie095.spring.chapter20th.example2nd.web.SpittleControllerManagedOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.export.assembler.InterfaceBasedMBeanInfoAssembler;
import org.springframework.jmx.export.assembler.MBeanInfoAssembler;
import org.springframework.jmx.export.assembler.MethodExclusionMBeanInfoAssembler;
import org.springframework.jmx.export.assembler.MethodNameBasedMBeanInfoAssembler;
import org.springframework.jmx.support.RegistrationPolicy;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jiajing Li
 * @date 2021-03-31 20:55:12
 */
@SuppressWarnings("all")
@Configuration
public class Config {

    @Bean
    public MBeanExporter mbeanExporter(SpittleController spittleController) {
        MBeanExporter exporter = new MBeanExporter();
        Map<String, Object> beans = new HashMap<>();
        beans.put("spitter:name=SpittleController", spittleController);
        exporter.setBeans(beans);
        return exporter;
    }

    @Bean
    public MBeanExporter mbeanExporter(SpittleController spittleController,
                                       MBeanInfoAssembler assembler) {
        MBeanExporter exporter = new MBeanExporter();
        Map<String, Object> beans = new HashMap<>();
        beans.put("spitter:name=SpittleController", spittleController);
        exporter.setBeans(beans);
        exporter.setAssembler(assembler);
        exporter.setRegistrationPolicy(RegistrationPolicy.IGNORE_EXISTING);
        return exporter;
    }

    @Bean
    public MethodNameBasedMBeanInfoAssembler nameBasedAssembler() {
        MethodNameBasedMBeanInfoAssembler assembler =
                new MethodNameBasedMBeanInfoAssembler();
        assembler.setManagedMethods(new String[] {
                "getSpittlesPerPage", "setSpittlesPerPage"});
        return assembler;
    }

    @Bean
    public MethodExclusionMBeanInfoAssembler exclusionAssembler() {
        MethodExclusionMBeanInfoAssembler assembler =
                new MethodExclusionMBeanInfoAssembler();
        assembler.setIgnoredMethods(new String[] {"spittles"});
        return assembler;
    }

    @Bean
    public InterfaceBasedMBeanInfoAssembler interfaceBasedAssembler() {
        InterfaceBasedMBeanInfoAssembler assembler =
                new InterfaceBasedMBeanInfoAssembler();
        assembler.setManagedInterfaces(
                new Class<?>[] {SpittleControllerManagedOperations.class});
        return assembler;
    }


}
