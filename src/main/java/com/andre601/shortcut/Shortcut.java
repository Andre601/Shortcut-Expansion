/*
 * Copyright 2020 Andre601
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.andre601.shortcut;

import com.andre601.shortcut.logger.LegacyLogger;
import com.andre601.shortcut.logger.LoggerUtil;
import com.andre601.shortcut.logger.NativeLogger;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.NMSVersion;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.StringJoiner;

public class Shortcut extends PlaceholderExpansion{
    
    private final File folder = new File(PlaceholderAPIPlugin.getInstance().getDataFolder() + "/shortcuts/");
    
    public Shortcut(){
        LoggerUtil logger = loadLogger();
        
        if(folder.mkdirs())
            logger.info("Created shortcuts folder.");
    }
    
    @Override
    public @Nonnull String getIdentifier(){
        return "shortcut";
    }
    
    @Override
    public @Nonnull String getAuthor(){
        return "Andre_601";
    }
    
    @Override
    public @Nonnull String getVersion(){
        return "VERSION";
    }
    
    @Override
    public String onRequest(OfflinePlayer player, @Nonnull String params){
        String[] values = params.split(":");
        if(values.length <= 0)
            return null;
        
        File file = new File(folder, values[0].toLowerCase() + ".txt");
        if(!file.exists())
            return null;
        
        String value;
        try{
            BufferedReader reader = Files.newBufferedReader(file.toPath());
            StringJoiner joiner = new StringJoiner("\n");
            
            String line;
            while((line = reader.readLine()) != null){
                joiner.add(line);
            }
            
            reader.close();
            value = joiner.toString();
        }catch(IOException ex){
            value = null;
        }
        
        if(value == null)
            return null;
        
        if(values.length > 1){
            MessageFormat format = new MessageFormat(value.replace("'", "''"));
            value = format.format(Arrays.copyOfRange(values, 1, values.length));
        }
        
        return PlaceholderAPI.setPlaceholders(player, value);
    }
    
    private LoggerUtil loadLogger(){
        if(NMSVersion.getVersion("v1_18_R1") != NMSVersion.UNKNOWN)
            return new NativeLogger(this);
        
        return new LegacyLogger();
    }
}
