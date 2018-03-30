# Map-Labeler
Automated Map Labeler Code-a-thon 2018

# Team
* Jacob Bernard
* Will Kercher
* Edric Yu

## Basic Usage:  
### Label a single map:  
The program will read the image from the map-filename, 
place the label on it, and output the file to the output directory.
Make sure the file has one of the supported extensions: .gif, .jpg, .png, .bmp   

```
java Labeler <map-filename> <label>
```  
Example 
```
java Labeler aruba-map.gif Aruba
```
For a label that has multiple words, use quotes:
```
java Labeler usa-map.gif "United States of America"
```
### Label multiple maps using a csv file:  
The program will take a csv formatted as filename,label  
Example  
>aruba-map.gif,Aruba  
>usa-map.gif,United States of America  
>zimbabwe-map.gif,Zimbabwe  

The program will read each image based on the filenames, 
place labels on them, and output the files to the output directory.
Make sure the file has the extension ".csv"
```
java Labeler <csv-file>
```  
Example 
```
java Labeler maps-with-names.csv
```

(TODO, tell them to add location??)

### Label multiple maps in a directory:  (Currently NOT supported)
The program will read each image in the directory, 
place labels on them based on the filenames, and output the files to the output directory.  
The files in the directory should be named with the desired label 
followed by the correct file extension.
> maps-directory/
> > Aruba.gif  
> > usa-map.gif  
> > Zimbabwe.gif

```
java Labeler <directory>
```  
Example 
```
java Labeler maps-directory/
```
This will result in the following output directory
> output/
> > Aruba.gif -- labelled as "Aruba"  
> > usa-map.gif -- labelled as "usa-map"  
> > Zimbabwe.gif -- labelled as "Zimbabwe"  

## Option Flags:
The following option flags can be used with any of the above 3 labelling methods.

### ```-watermark```  
If this flag is present, the label(s) will be written in bold in large letters in a block font (Sans Serif) with a low alpha 
Changing any of these value manually will override this flag
```
java Labeler aruba-map.gif Aruba -watermark
```

### ```-factbook```  
If this flag is present, the label(s) will be written target the color that is usually land in the world fact book images
Changing target color will override this flag
```
java Labeler aruba-map.gif Aruba -factbook
```

### ```-center```  
If this flag is present, the label(s) will be centered
-lx and -ly flags will be ignored if this flag is present
```
java Labeler aruba-map.gif Aruba -factbook
```

### ```-i```  
If this flag is present, the label(s) will be written in italics.
```
java Labeler aruba-map.gif Aruba -i
```

### ```-b```  
If this flag is present, the label(s) will be written in bold.
```
java Labeler aruba-map.gif Aruba -b
```

### ```-n```  
If this flag is present, double spaces will be interpreted as a new line.
```
java Labeler usa-map.gif "United States  of  America" -n
```

### ```-f <font-name>```
If this flag is present, the label(s) will be written in the specified font.  
To get a list of acceptable fonts, use the ```-fonts``` option.  
The default font is Times New Roman.
```
java Labeler aruba-map.gif Aruba -f Arial
```
Use quotes if the font name contains spaces.
```
java Labeler aruba-map.gif Aruba -f "Comic Sans MS"
```

### ```-s <size>```
If this flag is present, the label(s) will be written in the specified font size.  
The default font size is 16.
```
java Labeler aruba-map.gif Aruba -s 18
```

### ```-px <x-padding-scale>```
If this flag is present, the label(s)'s width will be padded the specified amount.  
The padding scale should be no less than 1.
The default x padding scale is 1.2
```
java Labeler aruba-map.gif Aruba -px 1.8
```

### ```-py <y-padding-scale>```
If this flag is present, the label(s)'s height will be padded the specified amount.  
The padding scale should be no less than 1.
The default y padding scale is 1.5
```
java Labeler aruba-map.gif Aruba -py 2.1
```

### ```-c <text-color>```
If this flag is present, the label(s)'s color will be set to text color
The color can be given as a text using standard java colors. (Look [here](https://docs.oracle.com/javase/7/docs/api/java/awt/Color.html) for a list of colors)  
(Note -- Text color is not currently supported for .gif files)
```
java Labeler aruba-map.gif Aruba -c cyan
```

You can also specify color using hex values
```
java Labeler aruba-map.gif Aruba -c 0x00ffff
```

### ```-lx <x-location>```
If this flag is present, the label(s) will touch the specified x coordinate on the file
```
java Labeler aruba-map.gif Aruba -lx 640
```

### ```-ly <y-location>```
If this flag is present, the label(s) will touch the specified y coordinate on the file
```
java Labeler aruba-map.gif Aruba -ly 512
```

### ```-a <alpha-value>```
If this flag is present, the label(s) will be set to the specified alpha (0 is transparent, 1 is opaque) 
```
java Labeler aruba-map.gif Aruba -a .45
```

### ```-tc <target-color```
If this flag is present, the label(s) will be more likely to contain more of the target color
```
java Labeler aruba-map.gif Aruba -tc cyan
```

You can also specify color using hex values
```
java Labeler aruba-map.gif Aruba -tc 0x00ffff
```

## Additional Option Flags  
The following flags should be used without any other arguments.  
The program will display information, and leave files unchanged.

### ```-fonts```
Get a listing of available fonts that your system can use.
```
java Labeler -fonts
```

### ```-help```
Get some helpful information.
```
java Labeler -help
```

### ```-ext```
Get a listing of valid file extensions.
```
java Labeler -ext
```

## Examples  
The following is what aruba-map looks like before it is passed into the program  
![aruba-map.gif](https://github.com/LightSys/Map-Labeler/blob/JacobREADME/aruba-map.gif)

The following line will place the label "Aruba" on the specified image where it does not collide with anything and is relatively close to the center
```
java Labeler aruba-map.gif Aruba
```
Output:  
![aruba-map-labeled.gif](https://github.com/LightSys/Map-Labeler/blob/JacobREADME/aruba-map-labeled.gif)


The following line will place the label "Island of Aruba" on the specified image with many arguments
```
java Labeler aruba-map.gif "Island  of  Aruba" -b -i -px 1.1 -py 1.9 -f "Arial" -s 32 -n
```
Output:  
![aruba-map-arguments.gif](https://github.com/LightSys/Map-Labeler/blob/JacobREADME/aruba-map-arguments.gif)

The following line will place the label "Aruba" on the specified image in a way that may be useful when using images from CIA World Factbook
```
java Labeler aruba-map.gif "Aruba" -watermark -factbook
```
Output:  
![aruba-map-factbook.gif](https://github.com/LightSys/Map-Labeler/blob/JacobREADME/aruba-map-factbook.gif)

The following line will not output any image and instead will print out a list of fonts because the "-font" flag is present
```
java Labeler aruba-map.gif Aruba -f "Arial" -s 32 -b -i -font
```
Output: (this will vary by machine)
AR BERKLEY  
AR BLANCA  
AR BONNIE  
AR CARTER  
AR CENA  
AR CHRISTY  
AR DARLING  
AR DECODE  
AR DELANEY  
AR DESTINE  
AR ESSENCE  
AR HERMANN  
AR JULIAN  
Arial  
Arial Black  
(etc)

