# Setup UI Tree Game Calculator

Currently prioritized.

Warning: There is a window breaking bug where should the user spam or hold F11, the screen will become unusable until the user is able to press CTRL-Q or exit out of the window.  Since the issue stems from the window spamming dispose() or setVisible(true/false) unintentionally, you may have to press CTRL-Q multiple times to get out of the window.

## How to Use
1. Ensure that you have Java on your computer, [OpenJDK](https://openjdk.org/) or [OracleJDK](https://www.oracle.com/java/technologies/downloads/).
2. Sample text.  <For when a stable release has occured and a .jar file exists>.

## Acknowledgements/Inspirations

| Source | Inspired | Explanation |
| :--- | :--- | :--- |
| [`Digital`](https://github.com/hneemann/Digital) | [`Setup-Tree UI`](https://github.com/nwinn-student/project-octo-java/tree/main/SetupUI-Tree-Game-Calculator) | Digital's UI design and approach to creating, editting, and deleting the various component elements inspired the approach that was taken within Setup-Tree UI. |

## Accessibility Notice

For those visually impaired, I am using [NVDA](https://www.nvaccess.org/download/) to test and ensure that the application, once it becomes that, will be usable.

For Windows visually impaired users without NVDA set up for Java applications:

1. Navigate to Control Panel.
2. Navigate to Ease of Access: Optimize Visual Display.
3. Traverse to until you hear Enable Java Access Bridge
4. Apply these changes.  Congratulations, you can now ear much more information regarding various applications than you could previously.