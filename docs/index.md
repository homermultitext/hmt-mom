---
layout: page
title: "HMT MOM:  mandatory, ongoing maintenance"
---


## Overview

Diplomatic editions of the HMT project relate text, physical object and documentary images.  Editors create these editions with simple delimited text files, and TEI-compliant XML documents.

MOM validates these documents to ensure that they comply with the HMT project standards.

## Installation

-   [prerequisites and setting up your repository](prereqs)


## Running MOM


From the root directory of your project, start an sbt console:

    sbt console

From the console, load the MOM script:

    :load scripts/mom.sc

If this loads successfuly, your repository is correctly organized. To validate a specific page, use the `validate` function of the script you loaded:


    validate("PAGE's URN")


## Under the hood

`mom.sc` depends on a code library hosted at <https://github.com/homermultitext/hmt-mom>.

-  [API documentation](api/org/homermultitext/hmtmom/index.html) for version **3.5.0**.
