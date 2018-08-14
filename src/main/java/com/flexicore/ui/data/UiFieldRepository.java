package com.flexicore.ui.data;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;

import java.util.List;

@PluginInfo(version = 1)
public class UiFieldRepository extends AbstractRepositoryPlugin {


    public void massMerge(List<?> list){
        for (Object o : list) {
            em.merge(o);
        }
    }

}
