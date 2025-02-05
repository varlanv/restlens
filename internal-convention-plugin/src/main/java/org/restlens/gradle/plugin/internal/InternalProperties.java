package org.restlens.gradle.plugin.internal;

import lombok.RequiredArgsConstructor;
import org.gradle.api.artifacts.VersionCatalog;
import org.gradle.api.artifacts.VersionConstraint;
import org.gradle.api.provider.Provider;

@RequiredArgsConstructor
public class InternalProperties {

    VersionCatalog versionCatalog;

    public static String name() {
        return "__internal_convention_properties__";
    }

    public String getLib(String name) {
        return versionCatalog.findLibrary(name)
            .map(maybeLib -> maybeLib.map(lib -> String.format("%s:%s:%s", lib.getGroup(), lib.getName(), lib.getVersion())))
            .map(Provider::getOrNull)
            .orElseThrow();

    }

    public String getVersion(String name) {
        return versionCatalog.findVersion(name)
            .map(VersionConstraint::getRequiredVersion)
            .orElseThrow();
    }
}
