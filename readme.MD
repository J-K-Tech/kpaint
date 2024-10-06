# KPAINT

mod that allows custom paintings (atm client-side only)


## usage
you can just place any picture in the `.minecraft`(or whatever equivalent is for you)`/paintings` folder.

just a lil warning: the size of the painting will be the size of the image divided by 16. so a 320x160 image will be 20x10 blocks, so read next section please

### how to set custom sizes
its very simple, just create a file with the ending of .xy, the syntax of the file is pretty simple. the first line of the file will just be a number that relates to width and the second is the one of the height.

so if ur png is ezra.png the file name will be ezra.xy

the syntax is pretty strict so please just write one number in either of the two first lines (all other lines are ignored by the loader)

the width and height are measured by blocks, so if u want ur painting to be scaled to 2x3 (first being width and second being height) blocks your file will look like

2
3

### how to exclude the image from automatically adding

if you want to exclude your image from the automatic adding of my mod just put a dot at the start of the file name so you can just load them in your own code to do whatever you want.

### how to add animations

(there is no support for animated textures yet, next update will have it)

now if you want animated textures the structure is similar, but now you create a folder with the name of the animation you want and inside the folders be numbered pngs

for example:
ezra/0.png
ezra/1.png
ezra/2.png
ezra/3.png
ezra/4.png

### how to set animation size and speed
to set the size just create a file this way:
ezra/xy

and to set the speed just create a file that contains a single number, the lower the number the faster the animation since it means how many ticks it will skip until next frame will show.

ezra/keyrate

### how to exclude the animation from automatically adding

if you want to exclude the animation for a custom code then also put a dot at the start of the file name.


### how to add custom logic

once you set up the project and it has kpaint as dependency, you create a class that extends the `Painting` class, you now override the tick method for custom logic, in case you want to load images in a custom way then re-define `public final boolean customimage` in your class to true and set `public BufferedImage img` to the desired image and the renderer does the rest so you wont have to worry about this and just about your custom code.