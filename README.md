# recitree

### Cloning repo
Open intellij and create a new project from version control. Enter `git@github.com:eciccone/recitree.git` into the URL field and click clone.

Once cloned, to simplify things we will all work on the same branch. Execute the following command.
```bash
git branch -M main
```

### Commiting to local repo
Once satisfied with your edits, add the changed files to the stage (make sure to save the files, otherwise changes will not register).
```bash
git add <file-1> <file-2> ...
```

Alternatively you can add all changed files to the stage.
```bash
git add .
```

Once the changed files are staged, you can commit the changes to your local repository.
```bash
git commit -m "message of commit"
```

This essentially saves a new version of the file into the version control system locally on your computer.

### Pushing to github

To push your changes to our master repo, hosted on github, execute the following command:

```bash
git push -u origin main
```

This will merge the changes of your local repository with the master repository hosted on github.

### Pulling from Github

If another team member makes a commit and pushes it to Github, your local repository will be one version behind the master repository. Trying to commit at this point will result in failure.

In order to update your local repository to reflect the master repository you must pull the changes.
 ```bash
 git pull
 ```

Please note that if you and another team member are working on the same file, pulling after they pushed their changes to github, and you commited the changes will result in a merge conflict. This generates a funky syntax into the file and you will be required to manually edit it.

For example, first person A makes changes to file 1. They commit and push the changes to Github.
Person B then makes changes to file 1, before pulling the changes person A made. Person B commits their changes, but when they go to push to Github, a failure occurs because their repository version is behind the master repository. Person B must execute a git pull.
But due to them editing the same file git does not know what changes to keep and what to discard. At this point, person B's file 1 will look quite unusual and will need to be manually edited.

To overcome this scenario communication is key. We should only make changes to the files we are required to implement.

If division of work is split between a single file, it would be ideal that the two team members involved waits for the other to finish. That is, the first team member
commits and push their changes, only then would the second team member execute a git pull, and then start making their changes to the file.

## Intellij configuration

Although intellij works just fine out of the box, some minor configuration is needed.

### VM Options for ControlFx

To allow some features to compile in ControlFx we must add a vm option to our run configuration.
Navigate to `Run`, and choose `Edit Configurations...`, make sure the project is selected under `Application`. Choose `Modify Options` and then choose `add VM options`.
Finally, add the following vm option:

```--add-exports javafx.base/com.sun.javafx.event=org.controlsfx.controls```