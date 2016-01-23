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
    ssh -p 2223 root@localhost shutdown now # Shutdown the VM

    # Create a snapshot ( shutdown VM first )
    mv system.qcow2 baseos.qcow2
    qemu-img create -f qcow2 -b baseos.qcow2 system.qcow2

    # Revert to snapshot ( shutdown VM first )
    rm system.qcow2
    qemu-img create -f qcow2 -b baseos.qcow2 system.qcow2

