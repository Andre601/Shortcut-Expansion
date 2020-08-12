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

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Shortcut extends PlaceholderExpansion{
    
    private final File folder = new File(PlaceholderAPIPlugin.getInstance().getDataFolder() + "/shortcuts/");
    
    public Shortcut(){
        if(folder.mkdirs())
            PlaceholderAPIPlugin.getInstance().getLogger().info("[Shortener] Created shortcuts folder.");
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
        File file = new File(folder, params.toLowerCase() + ".txt");
        if(!file.exists())
            return null;
        
        String value;
        try{
            value = String.join("\n", Files.readAllLines(file.toPath()));
        }catch(IOException ex){
            value = null;
        }
        
        if(value == null)
            return null;
        
        return PlaceholderAPI.setPlaceholders(player, value);
    }
    
}
