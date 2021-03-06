/*
 * $Id: CompressedLobSource.java,v 1.2 2002/12/16 23:34:55 rwald Exp $
 * =======================================================================
 * Copyright (c) 2002 Axion Development Team.  All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions 
 * are met:
 * 
 * 1. Redistributions of source code must retain the above 
 *    copyright notice, this list of conditions and the following 
 *    disclaimer. 
 *   
 * 2. Redistributions in binary form must reproduce the above copyright 
 *    notice, this list of conditions and the following disclaimer in 
 *    the documentation and/or other materials provided with the 
 *    distribution. 
 *   
 * 3. The names "Tigris", "Axion", nor the names of its contributors may 
 *    not be used to endorse or promote products derived from this 
 *    software without specific prior written permission. 
 *  
 * 4. Products derived from this software may not be called "Axion", nor 
 *    may "Tigris" or "Axion" appear in their names without specific prior
 *    written permission.
 *   
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT 
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY 
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE 
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * =======================================================================
 */

package org.axiondb.types;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.axiondb.AxionException;


/**
 * A {@link LobSource} that compresses/decompresses
 * the input/output streams using GZIP compression.
 * 
 * @version $Revision: 1.2 $ $Date: 2002/12/16 23:34:55 $
 * @author Rodney Waldhoff
 */
public class CompressedLobSource implements LobSource {    
    public CompressedLobSource(LobSource source) {
        _source = source;
    }

    public long length() throws AxionException {
        throw new AxionException("Can't get the length of compressed lob types.");
    }

    public void truncate(long length) throws AxionException {
        if(0 == length) {
            _source.truncate(length);
        } else {
            throw new AxionException("Length must be 0, found " + length + ".");
        }
    }

    public InputStream getInputStream() throws AxionException {
        try {
            return new GZIPInputStream(_source.getInputStream());
        } catch(IOException e) {
            throw new AxionException(e);
        }
    }

    public OutputStream setOutputStream(long pos) throws AxionException {
        if(pos != 0L) {
            throw new AxionException("Position must be 0, found " + pos + ".");
        } else {
            try {
                return new GZIPOutputStream(_source.setOutputStream(pos));
            } catch(IOException e) {
                throw new AxionException(e);
            }
        }
    }

    private LobSource _source = null;
}
