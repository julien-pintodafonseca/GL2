all:
	cd sources/ && $(MAKE)

# Make all files readable, and propagate the 'x' bit from user to
# group & other.
set-perms:
	find . -type f -exec chmod a+r {} \+
	find . -type d -exec chmod a+rx {} \+
	find . -perm -u+x -exec chmod a+x {} \+
