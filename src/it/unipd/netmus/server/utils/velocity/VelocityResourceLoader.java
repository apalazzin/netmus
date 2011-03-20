package it.unipd.netmus.server.utils.velocity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Vector;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

/**
 * Nome: VelocityResourceLoader.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 14 Marzo 2011
 */

public class VelocityResourceLoader extends ResourceLoader {
    private Vector<String> paths = null;

    @Override
    public long getLastModified(Resource arg0) {
        return arg0.getLastModified();
    }

    @Override
    public InputStream getResourceStream(String arg0)
            throws ResourceNotFoundException {
        for (String path : paths) {
            InputStream is = null;
            try {
                is = new FileInputStream(path + "/" + arg0);
                return is;
            } catch (FileNotFoundException e) {
                // skip
            }
        }
        throw new ResourceNotFoundException(arg0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init(ExtendedProperties arg0) {
        paths = arg0.getVector("path");
    }

    @Override
    public boolean isSourceModified(Resource arg0) {
        return false;
    }

}
