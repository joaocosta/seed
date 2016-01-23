seed
====

When experimenting with software, it's useful to have a blank canvas.
seed can bootstrap minimal centos/fedora OS disk that can run in qemu.

Usage
----------
    # Bootstrap base images
    boot config.fedora23 ~/cluster/box1
    boot config.centos7  ~/cluster/box2

    # Start them up
    ~/cluster/box1/run

    # Login to new box
    ssh -p 2223 root@localhost 

    # Shut it down
    ssh -p 2223 root@localhost shutdown now

    # Revert to initial snapshot ( shutdown VM first )
    rm ~/cluster/box1/system.qcow2
    qemu-img create -f qcow2 -b ~/cluster/box1/baseos.qcow2 ~/cluster/box1/system.qcow2

