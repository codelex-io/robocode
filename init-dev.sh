#!/usr/bin/env bash

setProperty(){
  awk -v pat="^$1=" -v value="$1=$2" '{ if ($0 ~ pat) print value; else print $0; }' $3 > $3.tmp
  mv $3.tmp $3
}

COMPILER_OUTPUT=`pwd`/out/production/RoboCodeChallenge
echo Setting compiler output to \'$COMPILER_OUTPUT\'

setProperty "robocode.options.development.path" $COMPILER_OUTPUT "./dist/config/robocode.properties"

