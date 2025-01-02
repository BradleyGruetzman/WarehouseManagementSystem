How to execute GUI code with Java on codespaces:

1. Download/install the necessary software. The following commands download, install and
setup what we need:
2. sudo apt-get update
3. sudo apt-get install -y tightvncserver
4. sudo apt-get install -y novnc websockify
5. vncserver :1

6. Open a new port and connect through a new bash shell. The first command creates a new
web socket:
7. websockify --web=/usr/share/novnc/ 6080 localhost:5901
8. This is then opened in the new browser tab; navigate to that tab, click on vnc.html, and connect
with your password. Since there is no GUI program running it will say there is nothing to
display. That is okay.
9. To run a GUI program we go back to the codespace tab, start a new bash shell, and navigate to
the program we want to run, compile etc. Then we set up the display:
export DISPLAY=:1
10. Then we can run the java program and see the GUI in the other tab.

11. Running the java program. If you see that the display is not presenting the window with all
decorations, add this statement to the Java program before creating the JFrame object:
JFrame.setDefaultLookAndFeelDecorated(true);
