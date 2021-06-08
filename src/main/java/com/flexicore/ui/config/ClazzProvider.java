package com.flexicore.ui.config;

import com.flexicore.model.Clazz;
import com.flexicore.ui.model.GridPreset;
import com.flexicore.ui.model.Preset;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.response.Clazzes;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@Extension
public class ClazzProvider implements Plugin {

    @Autowired
    @Lazy
    private Clazzes clazzes;

    @Qualifier("gridPresetClazz")
    @Bean
    @Lazy
    public Clazz gridPresetClazz(){
        return clazzes.getClazzes().stream().map(f->f.getClazz()).filter(f->f.getName().equals(GridPreset.class.getCanonicalName())).findFirst().orElse(null);
    }


    @Qualifier("presetClazz")
    @Bean
    @Lazy
    public Clazz presetClazz(){
        return clazzes.getClazzes().stream().map(f->f.getClazz()).filter(f->f.getName().equals(Preset.class.getCanonicalName())).findFirst().orElse(null);
    }
}
