seed
====

When experimenting with software, it's useful to have a blank canvas.
seed can bootstrap minimal centos/fedora OS disk images that can run in qemu.

Usage
----------
    # Bootstrap base images
    boot config.fedora23 ~/cluster/box1
    boot config.centos7  ~/cluster/box2

    # Start them up

    ## Using defaults
    ~/cluster/box1/run

    # Login to new box
    ssh root@127.0.1.1

    # Shut it down
    ssh root@127.0.1.1 shutdown now

    ## Optionally set startup config values in box.cfg
    echo HOST_INTERFACE=127.0.1.2 > ~/cluster/box1/box.cfg
    ~/cluster/box1/run

    ## Or override them command line
    ~/cluster/box2/run -i 127.0.1.3

    # Revert to initial snapshot ( shutdown VM first )
    qemu-img create -f qcow2 -b ~/cluster/box1/baseos.qcow2 ~/cluster/box1/new_image.qcow2
    rm ~/cluster/box1/running_image
    ln -vs ~/cluster/box1/new_image.qcow2 ~/cluster/box1/running_image

