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
    ~/cluster/box1/run -i 127.0.1.1
    ~/cluster/box2/run -i 127.0.1.2

    # Login to new box
    ssh root@127.0.1.1

    # Shut it down
    ssh root@127.0.1.1 shutdown now

    # Revert to initial snapshot ( shutdown VM first )
    qemu-img create -f qcow2 -b ~/cluster/box1/baseos.qcow2 ~/cluster/box1/new_image.qcow2
    rm ~/cluster/box1/running_image
    ln -vs ~/cluster/box1/new_image.qcow2 ~/cluster/box1/running_image

