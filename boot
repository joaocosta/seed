#!/bin/bash

if [ -z "$1" ]
  then
  echo -e "Usage: $0 CONFIG_FILE"
  exit 1
fi

set -euo pipefail

source $1

echo Fetching kernel and initrd image if needed
wget -N $VMLINUZ_URL
wget -N $INITRD_URL

# Injecting the kickstart file into the initrd image prevents the dependency
# on an external PXE boot or HTTP server to serve the kickstart file
echo Injecting kickstart file in the initrd image.
cp initrd.img initrd_ks.img
echo $KS_FILE | cpio -c -o >> initrd_ks.img

echo Creating new system image disk file
rm -f system.qcow2
qemu-img create -f qcow2 -o preallocation=falloc system.qcow2 $SYSTEM_DISK_SIZE

### Boot from kernel and initrd image which will 
### do an OS install from the provided kickstart file
echo Booting system and installing via kickstart
sudo qemu-system-x86_64     \
        -drive file=system.qcow2,format=qcow2,index=0,media=disk     \
        -net nic -net user  \
        -m $MEMORY_SIZE     \
        -localtime          \
        -enable-kvm         \
        -no-reboot          \
        -kernel vmlinuz     \
        -initrd initrd_ks.img   \
        -append "ks=file:/$KS_FILE console=ttyS0 panic=1" \
        -nographic
