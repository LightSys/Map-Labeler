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
Make sure the file has one of the supported extensions: .gif, .jpg,   
(TODO LIST HERE)
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

### Label multiple maps in a directory:  
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

##Examples
aruba-map.gif


usa-map.gif



```
java Labeler aruba-map.gif Aruba
```

```
java Labeler aruba-map.gif Aruba -b
```


```
java Labeler aruba-map.gif Aruba -i
```


```
java Labeler usa-map.gif "United States of America"
```


```
java Labeler usa-map.gif "United States  of  America" -n
```

```
java Labeler aruba-map.gif Aruba -f "Comic Sans MS"
```

```
java Labeler aruba-map.gif Aruba -s 18
```

```
java Labeler aruba-map.gif Aruba -px 1.8
```

```
java Labeler aruba-map.gif Aruba -py 2.1
```


## Additional Examples

```
java Labeler aruba-map.gif Aruba -b -i
```

```
java Labeler aruba-map.gif Aruba -px 1.1 -py 1.9
```

```
java Labeler aruba-map.gif Aruba -f "Arial" -s 32
```


```
java Labeler aruba-map.gif Aruba -f "Arial" -s 32 -px 1.1 -py 1.9 -b -i
```

```
java Labeler aruba-map.gif Aruba -f "Arial" -s 32 -b -i -fonts
```