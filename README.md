# Shortcut

<a href="https://discord.gg/6dazXp6" target="_blank">
  <img alt="discord" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/minimal/social/discord-singular_vector.svg" height="64" align="right">
</a>
<a href="https://app.revolt.chat/invite/74TpERXA" target="_blank">
  <img alt="revolt" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/minimal/social/revolt-singular_vector.svg" height="64" align="right">
</a>

The Shortcut expansion allows to take content from TXT files, parse placeholders in them and return the final value. Useful for parsing of larger strings or as a workaround for very nested placeholders.

## How to use
You first have to download the expansion from the eCloud. You can do so by running `/papi ecloud download shortcut` followed by `/papi reload` to register it.  
This should create a `shortcut` folder located inside the `PlaceholderAPI` plugin folder.

You can now add and create TXT files inside this folder and fill them with any text you like (including line breaks, [custom replacement placeholders](#custom-replacement-placeholders) and placeholders from other expansions).  
Once you saved your changes can you just use the file name (Without its `.txt` extension) in the Shortcut placeholder and the expansion will get the text, parse placeholders and return the result.

> **Note**  
> Make sure to read the section below about [custom replacement placeholders](#custom-replacement-placeholders) and the required Placeholder syntax if you used them.

## Custom replacement placeholders
Since version 1.1.0 can you add custom replacements. These allow more dynamic TXT files through providing replacement values inside the placeholder itself.  
You can insert replacements by using the `{n}` placeholder (`n` being a number starting at 0) in the TXT file's content, which will then be replaced by whatever replacement has been provided in the placeholder.

Replacements are defined and separated using colons (`:`).

### Example
Assume we have a file called `example.txt` and filled it with the following content:  
```
This is an example displaying custom replacements!
The first argument is {0}
The second argument is {1}
```
Using the placeholder `%shortcut_example:apple:banana%` will turn it into:  
```
This is an example displaying custom replacements!
The first argument is apple
The second argument is banana
```

> **Note**  
> Keep in mind that the placeholders start at 0, meaning that `{0}` is replaced with the first replacement, `{1}` with the second and so on.
