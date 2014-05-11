#!/bin/bash

set -e

wget http://download.fedoraproject.org/pub/fedora/linux/releases/20/Fedora/x86_64/os/images/pxeboot/vmlinuz
wget http://download.fedoraproject.org/pub/fedora/linux/releases/20/Fedora/x86_64/os/isolinux/initrd.img
echo fedora20.ks | cpio -c -o >> initrd.img

dd if=/dev/zero of=system.raw bs=1024M count=6
dd if=/dev/zero of=data.raw bs=1024M count=20


sudo qemu-system-x86_64 -hda system.raw -hdb data.raw -net nic -net user -m 1024M -localtime -enable-kvm -kernel vmlinuz -initrd initrd.img -append "ks=file:///fedora20.ks" #-nographic

exit
qemu-system-x86_64
    -cdrom Fedora-Live-Desktop-x86_64-20-1.iso  \
    -hda system.raw                             \
    -hdb data.raw                               \
    -nographic


