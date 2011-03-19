package it.unipd.netmus.server.utils.velocity;

import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;

/**
 * Nome: VelocityEngineManager.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 14 Marzo 2011
 * 
 */

public final class VelocityEngineManager {
    static VelocityEngine engine = new VelocityEngine();
    static boolean init = false;

    public static void init(){
        if (!init) {
            //engine = new VelocityEngine();
            engine.setProperty("resource.loader", "file");
            engine.setProperty("file.resource.loader.class","it.unipd.netmus.server.utils.velocity.VelocityResourceLoader");
            engine.setProperty("file.resource.loader.path","templates");
            engine.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.Log4JLogChute" );
            try {
                engine.init();
                init=true;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    
    public static Template getTemplate(String template){
        try {
            return engine.getTemplate( template );
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        } catch (ParseErrorException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}

