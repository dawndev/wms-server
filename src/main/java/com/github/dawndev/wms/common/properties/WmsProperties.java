package com.github.dawndev.wms.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "wms")
public class WmsProperties {

    private ShiroProperties shiro = new ShiroProperties();

    private boolean openAopLog = true;

}
