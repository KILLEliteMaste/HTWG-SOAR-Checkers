#! /bin/sh

Xvfb :0 -ac -listen tcp -screen 0 1920x1080x24 &
sleep 3
/usr/bin/fluxbox -display :0 -screen 0 &
sleep 3
x11vnc -display :0.0 -forever -passwd ${X11VNC_PASSWORD:-password}