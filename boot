#!/bin/bash

set -e

source ./config

wget -N $VMLINUZ_URL
wget -N $INITRD_URL

cp initrd.img initrd_ks.img
echo $KS_FILE | cpio -c -o >> initrd_ks.img

if [ ! -f system.raw ]; then
    dd if=/dev/zero of=system.raw bs=1024k count=$SYSTEM_DISK_SIZE
fi

if [ ! -f data.raw ]; then
    dd if=/dev/zero of=data.raw bs=1024k count=$DATA_DISK_SIZE
fi

sudo qemu-system-x86_64     \
        -hda system.raw     \
        -hdb data.raw       \
        -net nic -net user  \
        -m $MEMORY_SIZE     \
        -localtime          \
        -enable-kvm         \
        -nographic          \
        -no-reboot          \
        -kernel vmlinuz     \
        -initrd initrd_ks.img   \
        -append "ks=file:///$KS_FILE console=ttyS0 panic=1"
