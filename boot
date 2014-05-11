#!/bin/bash

set -e

source ./config

#wget $VMLINUZ_URL
#wget $INITRD_URL
#echo $KS_FILE | cpio -c -o >> initrd.img

dd if=/dev/zero of=system.raw bs=1024k count=$SYSTEM_DISK_SIZE
dd if=/dev/zero of=data.raw bs=1024k count=$DATA_DISK_SIZE


sudo qemu-system-x86_64 -hda system.raw -hdb data.raw -net nic -net user -m $MEMORY_SIZE -localtime -enable-kvm -kernel vmlinuz -initrd initrd.img -append "ks=file:///$KS_FILE" #-nographic

exit
qemu-system-x86_64
    -cdrom Fedora-Live-Desktop-x86_64-20-1.iso  \
    -hda system.raw                             \
    -hdb data.raw                               \
    -nographic
