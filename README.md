# HMT-MOM: 1.0 release series#

Homer Multitext project: Mandatory Ongoing Maintenance.

Although HMT MOM can be installed and used on any system with git, gradle and the
Homer Multitext project's [fork of the Perseus morphological parser][morph], 
MOM is primarily intended for use from within 
the [Homer Multitext project virtual machine for editors][vm].

Detailed documentation on how to use MOM in editing material for the Homer Multitext
project is being added to the project web site's [guides for editors and contributors][2].

## Version numbering##

The left-most digit identifies a major version, defined as a version that either dramatically
changes MOM's functionality, or requires a change in configuration.  The second digit from the left
identifies an upgrade that adds some tests or functionality to the main version, but does not
require a change in configuration.  The third digit identifies a version with changes 
that do not change what MOM tests and validates (e.g., a version that improves formatting of HTML reports
or user interface).

## Further information##

See the [project wiki][1] for draft documentation,
and discussion of future developemnt plans.


[morph]: https://github.com/homermultitext/morpheus

[vm]: https://github.com/homermultitext/hmt-vm


[1]:  https://github.com/homermultitext/hmt-mom/wiki


[2]: http://www.homermultitext.org/hmt-docs/guides/index.html
