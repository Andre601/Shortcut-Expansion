# Shortcut
The Shortcut expansion is a very simple PlaceholderAPI expansion, which allows you to parse multiple placeholders and large text by simply having them in a TXT file.

## How to use
When you downloaded the expansion for the first time (`/papi ecloud download shortcut`) will the expansion create a `shortcuts` folder within the `PlaceholderAPI` folder.  
To now use the expansion, create or place TXT files in the shortcuts folder. In those files can you write any text you want with any PAPI placeholders you like.

Afterwards can you just use `%shortcut_<filename>%` to parse the content of the TXT file. `<filename>` has to be the name of the TXT file you put in the shortcuts folder WITHOUT the `.txt`, so `example.txt` would be `%shortcut_example%`.
