import os
import sys
import subprocess
import time





input_path = r"C:\Users\paulw\Desktop\numeric-PDDL_generator\pddl_gen\src\main\resources\input"
output_path = r"C:\Users\paulw\Desktop\numeric-PDDL_generator\pddl_gen\src\main\resources\output\pddl"
cost_model = "cost_model.txt"
variable_values = "variable_values.txt"


#cmd = r'java -jar '


if __name__ == "__main__":

    decl_path = os.path.join(input_path, "declare")
    pn_path = os.path.join(input_path, "petrinet")
    log_path = os.path.join(input_path, "logs")

    decl_f = os.listdir(decl_path)
    pn_f = os.listdir(pn_path)

    decl_f = [f for f in decl_f if f[-4:] == "decl"]
    pn_f = [f for f in pn_f if f[-4:] == "pnml"]

    pns = dict()
    logs = []

    for p_ in pn_f:
        pname = p_[:-5]

        if not (os.path.exists(os.path.join(decl_path, pname))):
            continue
        
        declmods = os.listdir(os.path.join(decl_path, pname))

        # Does log exists?
        if not (os.path.exists(os.path.join(log_path, f"{pname}.xes"))):
            continue   

        for f in declmods:
            
            if f[-4:] != "decl" or ("parsed" not in f):
                continue

            print(pname, f[-4:])

            var_sub_file = f.split("_parsed")[0]+".decl.txt"
            var_sub_file = f"variable_substitutions_{var_sub_file}"


            decl_loc = f"declare/{pname}\\{f}"
            pn_loc = f"petrinet/{p_}"
            log_loc = f"logs/{pname}.xes"
            var_sub_loc = f"variable_subs/{var_sub_file}"


            noise_ = [0.1, 0.2, 0.3]
            all_logs = [
                log_loc,
                f"logs/{pname}-0.1.xesstripped.xes",
                f"logs/{pname}-0.2.xesstripped.xes",
                f"logs/{pname}-0.3.xesstripped.xes",
            ]

            print(decl_loc, pn_loc, log_loc, var_sub_loc)


            new_folder_name = f.replace("_parsed.decl", "").replace("_", "-d")
            new_folder_path = os.path.join(output_path, new_folder_name)
            if not (os.path.exists(new_folder_path)):
                os.mkdir(new_folder_path)
                time.sleep(5)
            else:
                continue

            subprocess.call(['java', '-jar', r'C:\Users\paulw\Desktop\numeric-PDDL_generator\pddl_gen\target\pddl_gen-1.0-SNAPSHOT-launcher.jar', decl_loc, pn_loc, log_loc, variable_values, var_sub_loc, cost_model])


            output_path = r"C:\Users\paulw\Desktop\numeric-PDDL_generator\pddl_gen\src\main\resources\output\pddl"

            pddl_files = os.listdir(output_path)

            pddl_files = [pf for pf in pddl_files if pf[-4:] == "pddl" and "problem" in pf]

            [os.rename(os.path.join(output_path, pf), os.path.join(new_folder_path, pf)) for pf in pddl_files]

            time.sleep(5)

            for noise_level in noise_:
                if not (os.path.exists(os.path.join(new_folder_path, f"{noise_level}"))):
                    os.mkdir(os.path.join(output_path, f"{new_folder_name}-{noise_level}"))
                    print(f"logs/{pname}-{noise_level}.xesstripped.xes")
                    subprocess.call(['java', '-jar', r'C:\Users\paulw\Desktop\numeric-PDDL_generator\pddl_gen\target\pddl_gen-1.0-SNAPSHOT-launcher.jar', decl_loc, pn_loc, f"logs/{pname}-{noise_level}.xesstripped.xes", variable_values, var_sub_loc, cost_model])


                    output_path = r"C:\Users\paulw\Desktop\numeric-PDDL_generator\pddl_gen\src\main\resources\output\pddl"

                    pddl_files = os.listdir(output_path)

                    pddl_files = [pf for pf in pddl_files if pf[-4:] == "pddl" and "problem" in pf]

                    [os.rename(os.path.join(output_path, pf), os.path.join(os.path.join(output_path, f"{new_folder_name}-{noise_level}"), pf)) for pf in pddl_files]

                    time.sleep(5)


            
            print("-"*100)

        