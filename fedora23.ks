# https://docs.fedoraproject.org/en-US/Fedora/23/html/Installation_Guide/appe-kickstart-syntax-reference.html
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
rootpw --plaintext root
ignoredisk --only-use=sda
bootloader --location=mbr --timeout=0
zerombr
clearpart --all --initlabel
part swap --asprimary --fstype="swap" --size=1024
part /boot --fstype xfs --size=200
part pv.01 --size=1 --grow
volgroup rootvg01 pv.01
logvol / --fstype xfs --name=lv01 --vgname=rootvg01 --size=1 --grow


repo --name=rawhide --mirrorlist=http://mirrors.fedoraproject.org/mirrorlist?repo=rawhide&arch=$basearch

url --url="http://download.fedoraproject.org/pub/fedora/linux/releases/23/Server/x86_64/os"

%packages
@core
%end


%post --log=/root/postinstall.log

dnf -y update

mkdir -p /root/.ssh
chmod 700 /root/.ssh
echo "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQC6hE75Ox6wDfXVJzXeKdyUBO4o19TtGxboJTI2vR3CE9ZJbODIxSr+tfMZcwmuSF892PiahhVzAA2wJ6LdMtFH6FUIGvjU0i7jIo/x+TmvheH46N9qllo2C2ZlxL/HbpRYIyqEntUYcBQzYBvUwnzoDFgS1GhG4LalYp0U9zlHGOA/Wk7qBjH8Ca1mtPSnxudsb/NwERIjfLbvdX9Fc+vkx6fs3ykJv+p8lPEZkw3kcVAfuyhnXzE7kprSHDuOuQo0FDvCTjy9ISxZPvExKT7bD7vQRlrx9PLzYSWI7/evonWHR8c/jPS8U56ii8YH/rtC/iqo4LiwKFxoxaDdS2wD joaocosta@zonalivre.org" > /root/.ssh/authorized_keys
chmod 600 /root/.ssh/authorized_keys

dnf -y install augeas
augtool -s <<EOF
#root login needs to be enabled during initial setup so the project specific scripts can be executed
set /files/etc/ssh/sshd_config/PermitRootLogin yes
set /files/etc/ssh/sshd_config/Banner /etc/ssh/sshd-banner
EOF

cat /etc/system-release > /etc/ssh/sshd-banner

cat << 'EOF' >> /etc/ssh/sshd-banner
\ \        / / | |
 \ \  /\  / /__| | ___ ___  _ __ ___   ___
  \ \/  \/ / _ \ |/ __/ _ \| '_ ` _ \ / _ \
   \  /\  /  __/ | (_| (_) | | | | | |  __/
    \/  \/ \___|_|\___\___/|_| |_| |_|\___|

root password is 'root'
EOF

%end
