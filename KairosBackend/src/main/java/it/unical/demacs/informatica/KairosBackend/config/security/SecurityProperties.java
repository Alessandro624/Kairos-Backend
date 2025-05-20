package it.unical.demacs.informatica.KairosBackend.config.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "security")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SecurityProperties {
    private boolean enabled = true;
    private List<String> publicEndpoints = new ArrayList<>();
    private Map<String, String> protectedRoutes = new HashMap<>();
}
