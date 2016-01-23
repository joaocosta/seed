#!/bin/bash

if [ "$#" -ne 2 ]
  then
  echo -e "Usage: $0 CONFIG_FILE TARGET_DIR"
  exit 1
fi

set -euo pipefail
KICKSTART_DIR=$(dirname `readlink -e $0`)

source ${KICKSTART_DIR}/$1
TARGET_DIR=$2

if [ -d $2 ]; then
    echo -e "Directory already exists. Bailing out"
    exit 2
fi

WORK_DIR=`mktemp -d`

echo Fetching kernel and initrd image if needed
wget -N $VMLINUZ_URL -O ${WORK_DIR}/vmlinuz
wget -N $INITRD_URL -O ${WORK_DIR}/initrd.img

# Injecting the kickstart file into the initrd image prevents the dependency
# on an external PXE boot or HTTP server to serve the kickstart file
echo Injecting kickstart file in the initrd image.
cd ${WORK_DIR}
cp ${KICKSTART_DIR}/${KS_FILE} ${KS_FILE}
echo ${KS_FILE} | cpio -c -o >> initrd.img
cd -

echo Creating new system image disk file
mkdir -p ${TARGET_DIR}
cp ${KICKSTART_DIR}/run ${TARGET_DIR}/.
qemu-img create -f qcow2 -o preallocation=falloc ${TARGET_DIR}/baseos.qcow2 $SYSTEM_DISK_SIZE

### Boot from kernel and initrd image which will 
### do an OS install from the provided kickstart file
echo Booting system and installing via kickstart
sudo qemu-system-x86_64     \
        -drive file=${TARGET_DIR}/baseos.qcow2,format=qcow2,index=0,media=disk     \
        -net nic -net user  \
        -m 1024M            \
        -localtime          \
        -enable-kvm         \
        -no-reboot          \
        -kernel ${WORK_DIR}/vmlinuz     \
        -initrd ${WORK_DIR}/initrd.img   \
        -append "ks=file:/$KS_FILE console=ttyS0 panic=1" \
        -nographic

qemu-img create -f qcow2 -b ${TARGET_DIR}/baseos.qcow2 ${TARGET_DIR}/system.qcow2
rm -vfR ${WORK_DIR}
