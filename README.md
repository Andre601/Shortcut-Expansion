# Shortcut

<a href="https://discord.gg/6dazXp6" target="_blank">
  <img alt="discord" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/minimal/social/discord-singular_vector.svg" height="64" align="right">
</a>
<a href="https://app.revolt.chat/invite/74TpERXA" target="_blank">
  <img alt="revolt" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/minimal/social/revolt-singular_vector.svg" height="64" align="right">
</a>

The Shortcut expansion allows to take content from TXT files, parse placeholders in them and return the final value. Useful for parsing of larger strings or as a workaround for very nested placeholders.

## Placeholder

The following placeholder is available to use:

- `%shortcut_<file>%`

`<file>` is the name of a TXT file located in the `shortcuts` folder itself. Note that you should not include the `.txt` extension of the file itself, as the expansion will do this itself.  
Additionally can you also access files in subfolders inside the `shortcuts` folder. All you have to do is include the folder name in the `<file>` itself (i.e. `folder/file` will access `file.txt` in `shortcuts/folder/`).

## What is this expansion useful for?

This expansion's main use case is to bypass PlaceholderAPI's lack of nested placeholder support.  
PlaceholderAPI does not support placeholders within placeholders. An exception is expansions allowing the usage of the alternative bracket placeholder pattern (`{placeholder_text}`) which allows one level of nesting.

With the shortcut expansion can you define a TXT file containing the placeholder itself to parse using the default percent pattern (`%placeholder_text%`), allowing more nesting of placeholders.  

## Placeholder support

The expansion supports placeholders from other expansions through the bracket placeholder format (`{placeholder_text}`) but also own custom placeholders to use.

### Custom placeholders

Version `1.1.0` added support for defining your own placeholders that would be replaced by values you define in the shortcut placeholder itself.

To use custom placeholders, add `{n}` to your TXT file where `n` is a number starting at 0. Next, in the placeholder add a colon after the file followed by the text to use as replacement. PlaceholderAPI placeholders are supported as replacement option.  
Each replacement needs to be separated by a colon.

#### Example

Assume we have a file called `example.txt` inside the `shortcuts` folder with the following content:  
```
My favourite food is {0}
My least favourite food is {1}
```
If we now use the placeholder `%shortcut_example:burger:spinach%` will this turn the above text into this:
```
My favourite food is burger
My least favourite food is spinach
```

> [!IMPORTANT]
> - Numbers are zero-indexed, meaning `{0}` is the first argument, `{1}` the second argument and so on.
> - Should there be less arguments than placeholder values (As in there is a `{2}` while only 2 arguments are provided) will they not be changed.
> - Placeholders from PlaceholderAPI are parsed after custom placeholders.
