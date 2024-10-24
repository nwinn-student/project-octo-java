# Setup UI Tree Game Calculator

Currently prioritized, not stably released so expect bugs or incomplete sections.

> [!WARNING]
> Currently not optimized with regard to CPU or memory usage.

## How to Use (Regular Use)
1. Ensure that you have Java on your computer, [OpenJDK](https://openjdk.org/) or [OracleJDK](https://www.oracle.com/java/technologies/downloads/).  For users you only need a JRE, not a JDK.
2. Download [`TreeGameCalcDesigner`](https://github.com/nwinn-student/project-octo-java/tree/main/SetupUI-Tree-Game-Calculator/TreeGameCalcDesigner.zip), unzip it, and then open Tree-Game-Calc-Design.jar.  Keep the jar file within the folder, since it writes to clipboard.txt currently.
You can choose to leave the folder zipped, but it may lose some functionality, like cutting and copying.

## How to Use (Developer Use)
1. Ensure that you have Java on your computer, [OpenJDK](https://openjdk.org/) or [OracleJDK](https://www.oracle.com/java/technologies/downloads/).
2. Download [`Setup-Tree UI`](https://github.com/nwinn-student/project-octo-java/tree/main/SetupUI-Tree-Game-Calculator) and open up the folder using an IDE, such as BlueJ or Geany or VIM.

## Acknowledgements/Inspirations

| Source | Inspired | Explanation |
| :--- | :--- | :--- |
| [`Digital`](https://github.com/hneemann/Digital) | [`Setup-Tree UI`](https://github.com/nwinn-student/project-octo-java/tree/main/SetupUI-Tree-Game-Calculator) | Digital's UI design and approach to creating, editting, and deleting the various component elements inspired the approach that was taken within Setup-Tree UI. |
| [`Geany`](https://github.com/geany/geany) | [`Settings/Preferences`](https://github.com/nwinn-student/project-octo-java/blob/main/SetupUI-Tree-Game-Calculator/MenuBar.java) | Although the settings/preferences have yet to be started as of August 4th, 2024, they will take much inspiration from Geany in regards to the various settings Geany has.

## Accessibility Notice

For those visually impaired, I am using [NVDA](https://www.nvaccess.org/download/) to test and ensure that the application, once it becomes that, will be usable.

For Windows visually impaired users without NVDA set up for Java applications:

1. Navigate to Control Panel.
2. Navigate to Ease of Access: Optimize Visual Display.
3. Traverse downwards until you hear Enable Java Access Bridge.
4. Apply these changes.  Congratulations, you can now hear much more information regarding various applications than you could previously.

## Features

A list of features is provided below to provide information about what exists and what to expect.  Not complete.
| Feature | How to Use | Explanation |
| :--- | :--- | :--- |
| Undo/Redo Action | Click CTRL-Z for Undo and CTRL-Y for Redo | Allows for the user to undo or redo an action performed to a node, such as moving, creating, deleting, and editting.  The undo/redo feature is not like most, in that redo is not wiped after an action is performed. |
| Select All | Click CTRL-A for Select all | Selects all of the nodes on the screen, for deletion or rearranging |
| Fullscreen | Click F11 to Fullscreen | Allows the user to enter fullscreen mode, which can be exitted by clicking F11 again |


