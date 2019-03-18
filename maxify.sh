mkdir -p "./inputs-maxified"
mkdir -p "./inputs-maxified/experiment1"
mkdir -p "./inputs-maxified/experiment2"
mkdir -p "./inputs-maxified/experiment3"

mkdir -p "./maxified-solutions"
mkdir -p "./maxified-solutions/experiment1"
mkdir -p "./maxified-solutions/experiment2"
mkdir -p "./maxified-solutions/experiment3"

for f in ./inputs/experiment1/*.gm 
do
	filename="${f##*/}"
	extension="${filename##*.}"
	filename="${filename%.*}"

	touch "./inputs-maxified/experiment1/${filename}.maxpg"
	(
		cd ~/Desktop/pgsolver/bin;
		./transformer -mm ~/Desktop/2IMF35-AMC-A2/inputs/experiment1/${filename}.gm > ~/Desktop/2IMF35-AMC-A2/inputs-maxified/experiment1/${filename}.maxpg
		./pgsolver ~/Desktop/2IMF35-AMC-A2/inputs-maxified/experiment1/${filename}.maxpg -global recursive > ~/Desktop/2IMF35-AMC-A2/maxified-solutions/experiment1/${filename}.txt
	)
done


for f in ./inputs/experiment2/*.gm 
do
	filename="${f##*/}"
	extension="${filename##*.}"
	filename="${filename%.*}"

	touch "./inputs-maxified/experiment2/${filename}.maxpg"
	(
		cd ~/Desktop/pgsolver/bin;
		./transformer -mm ~/Desktop/2IMF35-AMC-A2/inputs/experiment2/${filename}.gm > ~/Desktop/2IMF35-AMC-A2/inputs-maxified/experiment2/${filename}.maxpg
		./pgsolver ~/Desktop/2IMF35-AMC-A2/inputs-maxified/experiment2/${filename}.maxpg -global recursive > ~/Desktop/2IMF35-AMC-A2/maxified-solutions/experiment2/${filename}.txt
	)
done


for f in ./inputs/experiment3/*.gm 
do
	filename="${f##*/}"
	extension="${filename##*.}"
	filename="${filename%.*}"

	touch "./inputs-maxified/experiment3/${filename}.maxpg"
	(
		cd ~/Desktop/pgsolver/bin;
		./transformer -mm ~/Desktop/2IMF35-AMC-A2/inputs/experiment3/${filename}.gm > ~/Desktop/2IMF35-AMC-A2/inputs-maxified/experiment3/${filename}.maxpg
		./pgsolver ~/Desktop/2IMF35-AMC-A2/inputs-maxified/experiment3/${filename}.maxpg -global recursive > ~/Desktop/2IMF35-AMC-A2/maxified-solutions/experiment3/${filename}.txt
	)
done
