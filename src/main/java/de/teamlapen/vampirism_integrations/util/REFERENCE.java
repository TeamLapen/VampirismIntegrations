package de.teamlapen.vampirism_integrations.util;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

public class REFERENCE {
    public static ArtifactVersion VERSION = new DefaultArtifactVersion("0.0.0");
    public static final String MODID = "vampirism_integrations";
    public static final String FORGE_VERSION_MIN = "14.23.0.2550";
    public static final String VAMPIRISM_VERSION_MIN = "1.5.4";

    public static final String VAMPIRISM_ID = "vampirism";
    public static final String DEPENDENCIES = "required-after:forge@[" + REFERENCE.FORGE_VERSION_MIN + ",);required-after:vampirism@[" + REFERENCE.VAMPIRISM_VERSION_MIN + ",];after:guideapi;after:mca@[1.12.2-5.3.1,);after:biomesoplenty;after:abyssalcraft";//TODO Vampirism Library
    public static final String VERSION_UPDATE_FILE = "http://maxanier.de/projects/vampirism/versions_integrations.json";
}
