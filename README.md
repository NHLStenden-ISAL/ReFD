# ReFD: ReFactoring Diagnosis

This repository houses the code for the ReFactoring Diagnosis (ReFD) Eclipse plugin of the Open University and NHL Stenden ISA Lab.

## Installation instructions

Before running the code, you need to install the correct tools and dependencies. Some of this can be done automatically by running ```setup_dev_environment.sh```.
Some parts of the installation have to be done by hand. During the installation, Eclipse will also ask you to trust some library dependencies. Without doing this, the
project will not run.

A curious bug has arisen for anyone using this tool in a Linux ARM64 system. The current version of Atlas uses JNI code in its database implementation. This compiled code
is available for MacOS (X86/ARM64), Windows (WIN32) and Linux (X86). It is however not available for Linux ARM64. Users who own a M1/M2 Mac and run Linux virtually will not
be able to start the Atlas toolchain because of this. A workaround is using Atlas's older updatesite. The version on that older site does not use the JNI implementation. The
specifics of this can be found in ```setup_dev_environment.sh```.