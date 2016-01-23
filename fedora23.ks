install
lang en_GB.UTF-8
keyboard uk
poweroff
timezone UTC
auth  --useshadow  --passalgo=sha512
selinux --disabled
firewall --disabled
services --enabled=NetworkManager,sshd
eula --agreed
rootpw --plaintext password
ignoredisk --only-use=sda
bootloader --location=mbr --timeout=0
zerombr
clearpart --all --initlabel
part swap --asprimary --fstype="swap" --size=1024
part /boot --fstype xfs --size=200
part pv.01 --size=1 --grow
volgroup rootvg01 pv.01
logvol / --fstype xfs --name=lv01 --vgname=rootvg01 --size=1 --grow


repo --name=base --baseurl=http://download.fedoraproject.org/pub/fedora/linux/releases/23/Server/x86_64/os

url --url="http://download.fedoraproject.org/pub/fedora/linux/releases/23/Server/x86_64/os"

%packages
@core
augeas
%end


%post --log=/root/postinstall.log

# Do the bare minimum so that I can ssh to the box and run a shell script to execute whatever provisioning needs happening

mkdir -p /root/.ssh
chmod 700 /root/.ssh
echo "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQC6hE75Ox6wDfXVJzXeKdyUBO4o19TtGxboJTI2vR3CE9ZJbODIxSr+tfMZcwmuSF892PiahhVzAA2wJ6LdMtFH6FUIGvjU0i7jIo/x+TmvheH46N9qllo2C2ZlxL/HbpRYIyqEntUYcBQzYBvUwnzoDFgS1GhG4LalYp0U9zlHGOA/Wk7qBjH8Ca1mtPSnxudsb/NwERIjfLbvdX9Fc+vkx6fs3ykJv+p8lPEZkw3kcVAfuyhnXzE7kprSHDuOuQo0FDvCTjy9ISxZPvExKT7bD7vQRlrx9PLzYSWI7/evonWHR8c/jPS8U56ii8YH/rtC/iqo4LiwKFxoxaDdS2wD joaocosta@zonalivre.org" > /root/.ssh/authorized_keys
chmod 600 /root/.ssh/authorized_keys


augtool -s <<EOF
#root login needs to be enabled during initial setup so the project specific scripts can be executed
set /files/etc/ssh/sshd_config/PermitRootLogin yes

#This saves time during vm startup
set /files/etc/grub.conf/timeout 0

#Removed because otherwise user install scripts can't use sudo
rm /files/etc/sudoers/Defaults[requiretty]
EOF

%end
