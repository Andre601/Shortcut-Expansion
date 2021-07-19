# Shortcut
The Shortcut expansion allows you to parse several placeholders and large text by providing them through TXT files.

## Syntax
The expansion has a very simple placeholder syntax:

### `%shortcut_<file>%`
`<file>` would be the name of the TXT file you want to use WITHOUT the `.txt` ending.

## How to use
You first need to download the expansion by using `/papi ecloud download shortcut` followed by `/papi reload`

After that should you see a `shortcuts` folder within the `plugins/PlaceholderAPI/` directory. This is the place where you put your TXT files to use.  
All you need to do now is creating a TXT file in it and add whatever text and placeholders you want to parse. After that just use the aforementioned [placeholder-syntax](#syntax) to get the parsed text.

## Where is this Useful?
This expansion is useful for when you want to parse either multiple placeholders or some large text and you're limited by certain factors (i.e. a plugin limitation or similar).  
It can also be useful as a workaround for cases where a plugin uses `{}` for own/internal placeholders and messes up other placeholders that use those brackets too or for cases where you have placeholders in placeholders in placeholders (i.e. `%placeholder1_{placeholder2_{placeholder3}}%`) which PlaceholderAPI would be unable to parse properly.
