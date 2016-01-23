seed
====

When experimenting with software, it's useful to have a blank canvas.
seed can bootstrap minimal centos/fedora OS disk that can run in qemu.

Usage
----------
    ./boot config.fedora23  # Bootstraps a disk image with a minimal fedora 23 installation.
    ./boot config.centos7  # Or alternatively, bootstrap centos7
    ./run # Starts a background qemu process running the created image
    ssh -p 2223 root@localhost # login to newly create VM
