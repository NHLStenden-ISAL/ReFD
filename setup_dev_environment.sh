sudo apt install default-jdk
wget -O eclipse_committers_2023_06.tar.gz "https://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/2023-06/R/eclipse-committers-2023-06-R-linux-gtk-aarch64.tar.gz&mirror_id=1"
if ! echo "46a853b0e1ba26f748e938ec178d1dacb23892c212675bf12199e79c239b89b0cae4ea1c17b761b229ddbaa8bf111c660151aa0f6ee16d281b2af2c76435412d  eclipse_committers_2023_06.tar.gz" | sha512sum --check; then
    echo "Checksum failed, Eclipse download possibly corrupted or malicious. Check file manually. Terminating script!"
    exit 1
fi
mkdir eclipse
tar -xzf eclipse_committers_2023_06.tar.gz -C eclipse
# https://stackoverflow.com/questions/7163970/how-do-you-automate-the-installation-of-eclipse-plugins-with-command-line
echo "Run eclipse and manually install the following additional softare (Help > Install New Software...):"
echo "Install Atlas from https://ensoftupdate.com/download/atlas/current (If on Linux ARM64 VM: https://download.ensoftcorp.com/atlas/eclipse/)"
echo "Install Toolbox Commons from https://ensoftcorp.github.io/toolbox-repository/"
