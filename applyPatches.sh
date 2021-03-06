#!/bin/bash

basedir=`pwd`
echo "  Rebuilding Forked projects.... "

function applyPatches {
    what=base/$1
    target=build/$1
    patches=$1

    cd $basedir/$what
    git branch -f upstream >/dev/null
    cd $basedir
    if [ ! -d  "$basedir/$target" ]; then
        git clone $what $target -b upstream
    fi

    cd "$basedir/$target"
    echo "  Resetting $target to $what..."
    git remote rm upstream 2>/dev/null 2>&1
    git remote add upstream ../../$what >/dev/null 2>&1
    git checkout master >/dev/null 2>&1
    git fetch upstream >/dev/null 2>&1
    git reset --hard upstream/upstream

    echo "  Applying patches to $target..."
    git am --abort

    if !(git am --3way $basedir/$patches/*.patch); then
        echo "  Something did not apply cleanly to $target."
        echo "  Please review above details and finish the apply then"
        echo "  save the changes with rebuildPatches.sh"
        exit $?
    else
        echo "  Patches applied cleanly to $target"
    fi
}

applyPatches Bukkit
applyPatches CraftBukkit
