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
import com.andre601.shortcut.logger.LoggerWrapper;
import com.andre601.shortcut.logger.NativeLogger;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.Cacheable;
import me.clip.placeholderapi.expansion.NMSVersion;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Locale;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Shortcut extends PlaceholderExpansion implements Cacheable{

    private final File folder = new File(PlaceholderAPIPlugin.getInstance().getDataFolder() + "/shortcuts/");
    private final Cache<String, String> cache = CacheBuilder.newBuilder()
        .expireAfterWrite(1, TimeUnit.MINUTES)
        .build();
    private final Pattern replacementPattern = Pattern.compile("\\{(\\d+)}");
    
    private final LoggerWrapper logger;
    
    private LastLog lastLog = null;

    public Shortcut(){
        logger = loadLogger();

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
        return "1.4.0";
    }
    
    @Override
    public void clear(){
        cache.invalidateAll();
    }

    @Override
    public String onRequest(OfflinePlayer player, @Nonnull String params){
        String[] values = params.split(":");
        if(values.length == 0)
            return "ERR: Invalid Placeholder pattern received!";

        String filename = values[0] + (values[0].toLowerCase(Locale.ROOT).endsWith(".txt") ? "" : ".txt");
        
        String rawText;
        
        try{
            Path path = Paths.get(folder.getName(), filename).normalize();
            if(!path.startsWith("shortcuts" + File.separator)){
                return "ERR: Invalid File Path!";
            }
            
            rawText = cache.get(filename.toLowerCase(Locale.ROOT), () -> {
                File file = new File(folder, filename);
                if(!file.exists())
                    return "ERR: No file with name " + filename + " exists!";
                
                try(BufferedReader reader = Files.newBufferedReader(file.toPath())){
                    StringJoiner joiner = new StringJoiner("\n");
                    String line;
                    
                    while((line = reader.readLine()) != null)
                        joiner.add(line);
                    
                    return joiner.toString();
                }catch(IOException ex){
                    sendCachedWarn("Encountered IOException while reading file " + file.getName() + "!", ex);
                    return "ERR: IOException on file read: " + ex.getMessage();
                }
            });
        }catch(Exception ex){
            sendCachedWarn("Encountered an Exception while handling input " + filename + "!", ex);
            return "ERR: Exception during Cache get: " + ex.getMessage();
        }
        
        if(rawText == null)
            return "ERR: No text was retrieved from file " + filename + "!";
        
        if(rawText.isEmpty())
            return rawText;
        
        // No placeholders. Return text as-is with PAPI placeholders parsed.
        if(values.length == 1)
            return PlaceholderAPI.setPlaceholders(player, rawText);
        
        return parseReplacements(player, rawText, Arrays.copyOfRange(values, 1, values.length));
    }
    
    private String parseReplacements(OfflinePlayer player, String text, String[] values){
        // Safety-check in case it still has no replacement despite above check...
        if(values.length == 0)
            return PlaceholderAPI.setPlaceholders(player, text);
        
        Matcher replacementMatcher = replacementPattern.matcher(text);
        String newText = text;
        
        if(replacementMatcher.find()){
            StringBuilder buffer = new StringBuilder();
            
            do{
                int index;
                try{
                    index = Integer.parseInt(replacementMatcher.group(1));
                }catch(NumberFormatException ex){
                    continue;
                }
                
                if(index < 0 || (index + 1) > values.length)
                    continue;
                
                replacementMatcher.appendReplacement(buffer, values[index]);
            }while(replacementMatcher.find());
            
            replacementMatcher.appendTail(buffer);
            newText = buffer.toString();
        }
        
        return PlaceholderAPI.setPlaceholders(player, newText);
    }

    private LoggerWrapper loadLogger(){
        if(NMSVersion.getVersion("v1_18_R1") != NMSVersion.UNKNOWN)
            return new NativeLogger(this);

        return new LegacyLogger();
    }
    
    private void sendCachedWarn(String msg, Exception ex){
        if(lastLog != null && !lastLog.isExpired())
            return;
        
        lastLog = new LastLog();
        logger.warn(msg, ex);
    }
    
    private static class LastLog{
        private final long timestamp;
        
        public LastLog(){
            timestamp = System.currentTimeMillis();
        }
        
        public boolean isExpired(){
            // Consider expired if older than 10 seconds;
            return System.currentTimeMillis() - timestamp > 10_000L;
        }
    }
}